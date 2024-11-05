import { Fragment } from 'react';
import { unescape } from 'he';
import './EventModal.scss';
import CloseableModal from './CloseableModal';
import useDateFormat from '../hooks/useDateFormat';
import ReactMarkdown from 'react-markdown'

export type EventInfo = {
  start: string,
  end: string,
  title: string,
  url: string,
  extendedProps: Record<string, any>,
}

function capitalize(txt: string) {
  return txt.charAt(0).toUpperCase() + txt.slice(1)
}

function displayKV(key: string, value: any) {
  return <Fragment key={key}>
    <h3>{capitalize(key)}</h3>
    {value}
  </Fragment>
}

function getEventTitle(event: EventInfo): [string, string] {
  const desc: string = event.extendedProps?.description || ""
  if (event.title.endsWith("...")) {
    const found = desc.match(/[^\n]+/)
    if (found) {
      return [found[0].trim(), desc.substring(found[0].length).trim()]
    }
  }
  return [event.title, desc]
}

function replaceDashBasedTitleWithHorizontalLine(input: string): string {
  return input.replaceAll(/([^\n])\n(---+)/g, "$1\n\n$2")
}

function renderMarkdown(md: string) {
  //return <p className="p-with-newlines">{replaceDashBasedTitleWithHorizontalLine(md)}</p>
  return <ReactMarkdown
    components={{
      h1: 'h4',
      h2: 'h5',
      h3: 'h6',
      h4: 'h6',
      h5: 'h6',
      p: ({ node, ...props }) => <p {...props} className="p-with-newlines"></p>
    }} >
    {replaceDashBasedTitleWithHorizontalLine(md)}
  </ReactMarkdown>
}

export default function(props: { event?: EventInfo, onDismiss: () => void }) {
  const dateFormats = useDateFormat()
  if (props.event === undefined) {
    return null
  }
  const event = props.event
  const [eventTitle, eventDesc] = getEventTitle(event)
  return <CloseableModal
    visibilityCtrl={[event !== undefined, props.onDismiss]}
    title={unescape(eventTitle)}
  >
    {displayKV("Time",
      <p>{dateFormats.formatDateTimeRange(new Date(event.start), new Date(event.end))}</p>
    )}
    {displayKV("URL", <a href={event.url} target="_blank">{event.url}</a>)}
    {Object.keys(event.extendedProps)
      .filter(k => k !== "description")
      .map((extraKey: string) => {
        if (event.extendedProps[extraKey] === "" || event.extendedProps[extraKey] === null) {
          return null
        }
        return displayKV(
          extraKey,
          renderMarkdown(unescape(event.extendedProps[extraKey]))
        )
      })}
    {displayKV(
      "description",
      renderMarkdown(unescape(eventDesc))
    )}
  </CloseableModal>
}
