import { Dispatch, SetStateAction } from 'react';
import CloseableModal from './CloseableModal';
import './ModalAbout.scss';

function link(title: string, href: string) {
  return <a href={href} target="_blank">{title}</a>
}

export default function ModalAbout(props: {
  visibilityCtrl: [boolean, Dispatch<SetStateAction<boolean>>]
}) {
  return (

    <CloseableModal
      visibilityCtrl={props.visibilityCtrl}
      title="Front-end for Clojure events calendar feed" >

      <p>
        This is a web-only preview of the calendar located {link(
          "here",
          "https://madclj.com/feeds/events.ics"
        )}. The calendar can also be shown by mobile phone calendar.
      </p>
      <h3>Authors</h3>
      <p>
        MadCalendar Web UI ({link(
          "Source code",
          "https://github.com/madclj/madclj.com/tree/main/madcalendar"
        )}): Ambrose Bonnaire-Sergeant
      </p>
      <p>
        Derived from CljCalendar ({link(
          "Source code",
          "https://gitlab.com/invertisment/cljcalendar/"
        )}): Martynas Maciulevičius
      </p>
      <p>
        Heavy lifting (scraping, aggregation into ical): Gert Goet
      </p>

    </CloseableModal>
  );
}
