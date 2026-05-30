#!/usr/bin/env bash
set -euo pipefail

SERIAL="${SERIAL:-emulator-5554}"
PKG="${PKG:-eu.slickbot.vremedo.debug}"
ACTIVITY="$PKG/eu.slickbot.vremedo.MainActivity"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="${OUT_DIR:-$REPO_ROOT/docs/screenshots}"
PREFS_PATH="/data/data/$PKG/shared_prefs/prefs-vremedo.xml"
LOAD_WAIT="${LOAD_WAIT:-10}"
RADAR_WAIT="${RADAR_WAIT:-9}"

a() { command adb -s "$SERIAL" "$@"; }
demo() { a shell am broadcast -a com.android.systemui.demo -e command "$@" >/dev/null; }

require_device() {
  command adb -s "$SERIAL" get-state >/dev/null 2>&1 || {
    echo "device '$SERIAL' not found (set SERIAL=...); connected:" >&2
    command adb devices >&2
    exit 1
  }
  a shell pm list packages "$PKG" | grep -q "$PKG" || {
    echo "$PKG not installed on $SERIAL; run with --install or: ./gradlew :app:installDebug" >&2
    exit 1
  }
}

build_install() {
  echo "installing debug build..."
  (cd "$REPO_ROOT" && ./gradlew :app:installDebug)
}

enable_demo() {
  a shell settings put global sysui_demo_allowed 1 >/dev/null
  demo enter
  demo battery -e level 100 -e plugged false
  demo network -e wifi show -e level 4
  demo network -e mobile hide
  demo notifications -e visible false
}

seed_prefs() {
  local sunrise="$1" sunset="$2" date
  date="$(a shell date +%F | tr -d '\r')"
  a shell run-as "$PKG" mkdir -p shared_prefs
  printf '%s\n' \
    "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>" \
    "<map>" \
    "    <string name=\"day_date\">$date</string>" \
    "    <string name=\"day_sunrise\">$sunrise</string>" \
    "    <string name=\"day_sunset\">$sunset</string>" \
    "</map>" \
  | a shell "run-as $PKG sh -c 'cat > $PREFS_PATH'"
}

tap_node() {
  local pred="$1" bounds
  a shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  bounds="$(a shell cat /sdcard/ui.xml | tr '>' '\n' \
    | grep -E "$pred" \
    | grep -oE 'bounds="\[[0-9]+,[0-9]+\]\[[0-9]+,[0-9]+\]"' | head -1)"
  [ -n "$bounds" ] || { echo "ui node not found: $pred" >&2; return 1; }
  echo "$bounds" \
    | sed -E 's/.*\[([0-9]+),([0-9]+)\]\[([0-9]+),([0-9]+)\].*/\1 \2 \3 \4/' \
    | { read -r x1 y1 x2 y2; a shell input tap $(((x1 + x2) / 2)) $(((y1 + y2) / 2)); }
}

shot() { a exec-out screencap -p > "$OUT_DIR/$1"; echo "  -> $1"; }

launch() {
  a shell am force-stop "$PKG"
  a shell am start -n "$ACTIVITY" >/dev/null 2>&1
  sleep "$LOAD_WAIT"
  a shell pidof "$PKG" >/dev/null || {
    echo "app died on launch (likely OOM); give the emulator more RAM, e.g. relaunch with -memory 4096" >&2
    exit 1
  }
}

capture_theme() {
  local theme="$1" sunrise="$2" sunset="$3" hhmm="$4"
  echo "$theme:"
  a shell am force-stop "$PKG"
  seed_prefs "$sunrise" "$sunset"
  demo clock -e hhmm "$hhmm"
  launch
  shot "weather-$theme.png"
  tap_node 'content-desc="menu"'
  sleep 2
  shot "menu-$theme.png"
  tap_node 'text="Radar"'
  sleep "$RADAR_WAIT"
  shot "radar-$theme.png"
}

main() {
  [ "${1:-}" = "--install" ] && build_install
  require_device
  mkdir -p "$OUT_DIR"
  enable_demo
  capture_theme light "00:00" "23:59" 1200
  capture_theme dark "00:00" "00:01" 2300
  demo exit
  echo "done -> $OUT_DIR"
}

main "$@"
