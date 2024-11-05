import { Dispatch, ReactNode, SetStateAction } from 'react';
import Modal from 'react-modal';
import { appRootHtmlId } from '../constants';
import './CloseableModal.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faXmark } from '@fortawesome/free-solid-svg-icons'

// Make sure to bind modal to your appElement (https://reactcommunity.org/react-modal/accessibility/)
Modal.setAppElement(document.getElementById(appRootHtmlId) as HTMLElement);

export default function(props: {
  title: string,
  children: ReactNode,
  onDismiss?: () => void,
  visibilityCtrl: [boolean, Dispatch<SetStateAction<boolean>>]
}) {
  const [isOpen, setIsOpen] = props.visibilityCtrl
  return <Modal
    isOpen={isOpen}
    onAfterClose={props.onDismiss}
    onRequestClose={() => { setIsOpen(false) }}
    shouldCloseOnEsc={true}
    contentLabel={props.title}
    style={{
      overlay: {
        height: "auto",
        width: "auto",
      },
      content: {
        maxWidth: 600,
        height: "auto",
        margin: "auto",
        top: "1%",
        left: "1%",
        right: "1%",
        bottom: "1%",
      },
    }}
  >
    <div className="closeableModalCloseButton">
      <h2>{props.title}</h2>
      <button
        onClick={() => setIsOpen(false)}
        className="textButton"
        style={{ height: "fit-content" }}>
        <FontAwesomeIcon icon={faXmark} size="2x" />
      </button>
    </div>
    {props.children}
    <button
      onClick={() => setIsOpen(false)}
      style={{ padding: 10, margin: "auto", display: "block", border: "none", borderRadius: "0.25em" }}>Close</button>
  </Modal>
}
