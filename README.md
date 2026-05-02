# madclj.com

Website for Madison Clojure, a Clojure user group in Madison, Wisconsin.

![Madison Clojure](static/images/madclj-logo.jpg)

## Quick Start

Install [Hugo](https://gohugo.io/installation/), then:

```bash
./serve.sh  # starts server at http://localhost:1313
```

---

## For Organizers: Creating & Managing Meetups

This repo uses a dual-record system:
- **Calendar** (`events.clj`): Generic titles/descriptions, auto-synced to madclj.com and .ics feeds
- **Discussions** (GitHub): Detailed agenda, topics, RSVPs, updates

⚠️ **These do NOT auto-sync.** Changes must be made in both places if you modify time/date/location.

### Create a New Event

**1. Create a GitHub discussion** at https://github.com/orgs/madclj/discussions
   - Write the title, agenda, topics, and any prep needed
   - Pin the discussion (only pinned discussions count for RSVP metrics)
   - Copy the discussion URL

**2. Add event to `feeds/src/madison_clojure/events.clj`**

```clojure
{:full-title "January 14th Meetup"
 :summary "January 14th Meetup"
 :uid "https://github.com/orgs/madclj/discussions/37"
 :rsvp "https://github.com/orgs/madclj/discussions/37"
 :description (text "Generic description or TBD")
 :location startingblock  ; or startingblock-pop for 3rd floor
 :start (madison-time "2026-01-14T18:30")
 :end (madison-time "2026-01-14T21:00")}
```

Keep `:summary` and `:description` generic—specific details live in the GitHub discussion.

**3. Commit & push**
   - Create a PR (main branch is protected)
   - As a maintainer, merge with bypass
   - For quick edits, use GitHub's web editor for `events.clj`

### Update or Cancel an Event

**To change time/date/location:**
- Update `events.clj` (commit via PR)
- Post a comment in the GitHub discussion
- Update the discussion title if needed

**To cancel:** Don't delete it. Instead:
- Add `:cancelled true` to the event
- Prefix titles with "CANCELLED "

```clojure
{:full-title "CANCELLED May 14th Meetup"
 :summary "CANCELLED May 14th Meetup"
 :cancelled true
 ...}
```

### Remind Attendees

Use `@madclj/meetup` in a discussion comment 3–7 days before the event.

### After the Event

1. **Update the discussion title** — add "DONE" prefix
2. **Unpin the discussion**
3. **Pin the next upcoming event** — so it's visible and collecting RSVPs

This keeps the calendar organized and ensures attendees see the current event.

### Plan a New Year

At the start of each year, create placeholder events (second Wednesday of each month):
- Use `seed-events-2026` in `events.clj` as a template
- Set `:description` to "TBD" initially
- Update with real topics as decisions are made

---

## FAQ

**RSVP counts not updating?**
- Ensure the discussion is pinned
- Check the discussion URL in `events.clj` matches exactly
- The workflow runs at 2 AM & 2 PM Madison time (or manually trigger from Actions tab)

**Event not on calendar?**
- Verify `:uid` matches the GitHub discussion URL exactly
- Check `:start` and `:end` times
- Confirm the workflow succeeded (github.com/madclj/madclj.com/actions)

---

## Technical Details

**Calendar generation:**
1. Edit `feeds/src/madison_clojure/events.clj`
2. Push to main
3. GitHub Actions workflow runs `script/gen-calendar`
4. Generates `events.ics`, updates `content/_index.markdown`
5. Hugo builds site; deploys to GitHub Pages

**Key files:**
- `feeds/src/madison_clojure/events.clj` — event definitions
- `script/build` — full build pipeline
- `.github/workflows/hugo.yaml` — CI/CD config

**Locations:**
- `startingblock` — 821 E Washington Ave, 2nd floor
- `startingblock-pop` — 821 E Washington Ave, 3rd floor ("Pop" conference room)

---

## Useful Links

- **Website**: https://madclj.com
- **Discussions**: https://github.com/orgs/madclj/discussions
- **Repository**: https://github.com/madclj/madclj.com
- **Actions/Workflows**: https://github.com/madclj/madclj.com/actions/workflows/hugo.yaml
