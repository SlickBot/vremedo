# TODO

General ideas to prune. We'll break them up later.

## Localization
- [ ] Extract hardcoded strings into `strings.xml` (currently a mix of SLO/EN).
- [ ] Slovenian + English, locale-aware weekdays/units/dates.
- [ ] In-app language override + Lint guard against new hardcoded text.

## Parser resilience
- [ ] Graceful per-screen error/retry states instead of blank/crash.
- [ ] Offline HTML-fixture tests for the parsers (some current tests hit live net).
- [ ] Scheduled CI job that runs parsers against live pages.

## Caching & offline
- [ ] Cache last successful data; open to something offline.
- [ ] "Last updated" timestamps + pull-to-refresh.
- [ ] Persist last-selected image/radar screen across launches.

## Features
- [ ] Favorite/pinned towns + nearest town via location.
- [ ] Home-screen widget(s) + severe-weather notifications from ARSO alerts.
- [ ] Sun/moon view, share screenshots, compare two towns.

## UI / UX
- [ ] Material You dynamic color.
- [ ] Reduce-motion support for the loops.

## Accessibility
- [ ] Finish content-description / TalkBack pass, font-scaling, contrast.

## Architecture & testing
- [ ] Compose screenshot tests (light/dark + both locales).
- [ ] Run lint + tests in CI (currently release-only); dependency-update bot.

## Distribution
- [ ] F-Droid + Play release; automate publishing the signed APK from CI.

## Stretch
- [ ] Wear OS tile, Quick Settings radar tile, KMP parsers.
