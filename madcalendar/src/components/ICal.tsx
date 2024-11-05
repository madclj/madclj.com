import FullCalendar, { EventClickArg } from '@fullcalendar/react';
import iCalendarPlugin from '@fullcalendar/icalendar'
import dayGridPlugin from '@fullcalendar/daygrid';
import './ICal.scss';
import listPlugin from '@fullcalendar/list';
import { useEffect, useRef, useState } from 'react';
import EventModal, { EventInfo } from './EventModal';
import { useGrid } from '../hooks/useDimensions';
import { useUrlState } from '../hooks/useUrlState';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCalendarDay, faInfoCircle, faListDots, faTableCells, IconDefinition } from '@fortawesome/free-solid-svg-icons'
import ModalAbout from './ModalAbout';
import { SwipeableProps, useSwipeable } from 'react-swipeable';
import useHashRouteToggle from '../hooks/useHashRouteToggle';
import useDateFormat from '../hooks/useDateFormat';

// https://fullcalendar.io/docs/typescript
// https://fullcalendar.io/docs/icalendar
// https://fullcalendar.io/docs/react

function getIcon(definition: IconDefinition, text?: string): string {
  if (text) {
    return ((<div>{<FontAwesomeIcon icon={definition} size="1x" />} {text}</div>) as unknown) as string
  }
  return ((<div>{<FontAwesomeIcon icon={definition} size="1x" />}</div>) as unknown) as string
}

function getResponsiveIcon(displayLarge: boolean, definition: IconDefinition, text: string): string {
  return (((getIcon(definition, displayLarge ? text : undefined)) as unknown) as string)
}

function mkSwipeConfig(calendarRef: React.MutableRefObject<FullCalendar>): SwipeableProps {
  return {
    onSwipedLeft: (_e) => calendarRef.current.getApi().next(),
    onSwipedRight: (_e) => calendarRef.current.getApi().prev(),
    ...{
      delta: 10,                             // min distance(px) before a swipe starts. *See Notes*
      preventScrollOnSwipe: false,           // prevents scroll during swipe (*See Details*)
      trackTouch: true,                      // track touch input
      trackMouse: false,                     // track mouse input
      rotationAngle: 0,                      // set a rotation angle
      swipeDuration: Infinity,               // allowable duration of a swipe (ms). *See Notes*
      touchEventOptions: { passive: true },  // options for touch listeners (*See Details*)
    },
  }
}

export default function(props: { src: { url: string, format: string } }) {
  const url = useUrlState()
  const [previewedEvent, setPreviewedEvent] = useHashRouteToggle<EventInfo | undefined>("event", (): EventInfo | undefined => undefined, undefined)
  const [showAsStack, setShowAsStack] = useState(url.state.stack)
  const [aboutModalOpen, setAboutModalOpen] = useHashRouteToggle<boolean>("about", (bool) => bool, false)
  const currentView = showAsStack
    ? {
      view: "mmListMonth",
      toggleButtonText: "Grid",
      icon: faTableCells
    }
    : {
      view: "mmDayGridMonth",
      toggleButtonText: "Stack",
      icon: faListDots
    }

  // https://stackoverflow.com/a/65039223
  const calendarRef = useRef() as React.MutableRefObject<FullCalendar>

  const handlers = useSwipeable(mkSwipeConfig(calendarRef));
  const dateFormats = useDateFormat()

  useEffect(() => {
    if (calendarRef !== undefined) {
      // https://github.com/fullcalendar/fullcalendar/issues/4684#issuecomment-620787260
      calendarRef.current.getApi().changeView(currentView.view)
    }
  }, [currentView])
  const grid = useGrid()
  return <div {...handlers} className="calendarStyleHacks">
    <>
      {aboutModalOpen && <ModalAbout visibilityCtrl={[aboutModalOpen, setAboutModalOpen]} />}
      {previewedEvent &&
        <EventModal
          event={previewedEvent}
          onDismiss={() => {
            setPreviewedEvent(undefined)
          }} />}
      <FullCalendar
        firstDay={1} // monday
        ref={calendarRef}
        plugins={[dayGridPlugin, listPlugin, iCalendarPlugin]}
        events={props.src}
        initialView={currentView.view}
        views={{
          mmDayGridMonth: {
            type: "dayGridMonth",
            initialView: "dayGridMonth",
          },
          mmListMonth: {
            type: "listMonth",
            initialView: "listMonth",
          },
        }}
        headerToolbar={{
          left: "title",
          //center: "",
          right: grid.atLeastSmall
            ? "mmInfoButton mmToggleStackButton,today prev,next"
            : "mmInfoButton mmToggleStackButton,today"
        }}
        locale={dateFormats.locales}
        titleFormat={
          grid.whRatioAtLeast1W
            ? dateFormats.yearMonthFormat
            : dateFormats.yearMonthShortFormat
        }
        eventTimeFormat={dateFormats.timeShortFormat}
        customButtons={{
          "mmInfoButton": {
            text: getResponsiveIcon(grid.whRatioAtLeast6W, faInfoCircle, "About"),
            hint: "View information about the creators and addition of events",
            click: (_ev: MouseEvent, _element: HTMLElement) => setAboutModalOpen(!aboutModalOpen),
          },
          "mmToggleStackButton": {
            text: getResponsiveIcon(grid.whRatioAtLeast4W, currentView.icon, currentView.toggleButtonText),
            hint: "Toggle between monthly stacked view and whole-month view",
            click: (_ev: MouseEvent, _element: HTMLElement) => {
              setShowAsStack(!showAsStack)
              url.replaceHistory({ stack: !showAsStack })
            },
          }
        }}
        aspectRatio={undefined}
        height="auto"
        buttonText={{
          today: getResponsiveIcon(grid.whRatioAtLeast3W, faCalendarDay, "Today")
        }}
        eventClassNames="event-name-size"
        viewClassNames={"z-index-zero"}
        eventClick={(arg: EventClickArg) => {
          arg.jsEvent.preventDefault()
          setPreviewedEvent({
            start: arg.event.startStr,
            end: arg.event.endStr,
            title: arg.event.title,
            extendedProps: arg.event.extendedProps,
            url: arg.event.url
          })
        }}
      />
    </>
  </div>
}
