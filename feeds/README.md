# feeds

## Usage

### Announce an event

Announcing an event is done by creating a new topic in the [Zulip event-stream](https://clojurians.zulipchat.com/#narrow/stream/262224-events).  
In order for an event to end up in the [calendar-feed](https://www.clojurians-zulip.org/feeds/events.ics) it needs to have a specific format ([described in the stream's README](https://clojurians.zulipchat.com/#narrow/stream/262224-events/topic/README)).  
The following CLI helps you do that.  
_Note that it might take some time for an event to show up in the calendar-feed as the CI runs only every hour or so._

Prerequisites:
- [Zulip API-key found in settings](https://clojurians.zulipchat.com/#settings/account-and-privacy)
- an event that's interesting for the wider Clojure community and remotely accessible (after signup or rsvp) to everyone

```bash
$ export ZULIP_AUTH='<zulip-email>:<api-key>'
# create alias for cli (no need to first clone this repository)
alias zulip_events='clojure -Sdeps '"'"'{:deps {clojurians-zulip/feeds {:git/url "https://gitlab.com/clojurians-zulip/feeds.git" :sha "7f6dfa52631639faedb8993d8c3de666ccdb22bd"}} :aliases {:cli/events {:main-opts ["-m" "inclined.main" "--ns" "clojurians-zulip.events" "--"]}}}'"'"' -M:cli/events'


# see help for all flags/options
$ zulip_events create -h

# create an event from a meetup.com event (you will be shown a preview before submitting):
$ zulip_events create --zulip-auth "${ZULIP_AUTH}" --url https://www.meetup.com/london-clojurians/events/288373088/

# ...from a clojureverse.org event
$ zulip_events create --zulip-auth "${ZULIP_AUTH}" --url https://clojureverse.org/t/data-recur-meeting-3-general-monthly-focusing-on-meander/9352

# manually:
$ zulip_events create --zulip-auth "${ZULIP_AUTH}" --title 'Some short title (< 45 chars)' --start '2021-01-13T03:00-07:00' --url http://some.option.al/url --description 'Some description'

# in order to not bother with escaping quotes in the description use pbpaste, a file or a here-doc:
$ ..... --description "$(pbpaste)"
$ ..... --description "$(cat description.txt)"
$ .....  --description "$(cat <<'EOF'
<paste stuff>
EOF
)"

```

### Development

``` bash
$ clojure -M:cli/events create --dev --zulip-auth "${ZULIP_AUTH}" ...
```

Note:
* you can optionally provide `--duration i` (default is 2 (hours))  
* a short title (< 45 chars) is preferred (Zulip has a max topic length, calendars show only so much etc).  
  Any extended title can go in the description.
* on successful exit, the CLI shows the url of the created topic  

## License

See [LICENSE](LICENSE).
