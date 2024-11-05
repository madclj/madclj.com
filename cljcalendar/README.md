# Front-end for Clojure Madison Clojure events calendar feed

Forked from https://gitlab.com/invertisment/cljcalendar/

This is a web-only preview of the calendar located [here](https://madclj.com/events.ics).
The calendar can also be shown by mobile phone calendar.

[Open the calendar](https://madclj.com/calendar)

## This is for Clojure but why react

It's based on React and not ClojureScript.

Typescript was fitting the problem better than re-frame.
I needed a pre-built calendar component and other than that there is no complex state.

## Running locally

Run on port 3000: `pnpm start`

Run tests (there are none): `pnpm test`

Production build: `pnpm build`

## Authors
Web UI: Martynas Maciulevičius

Heavy lifting (scraping, aggregation into ical): Gert Goet

## License
AGPLv3
