
type URLState = {
  //year?: string
  //month?: string
  stack: boolean
}

const viewParam = "view"

const stackParamValue = "stack"

function mergeState(currentState: URLState, expectedState: URLState): URL | undefined {
  const url = new URL(window.location as any);
  if (currentState.stack === expectedState.stack) {
    return undefined
  }
  if (expectedState.stack) {
    url.searchParams.set(viewParam, stackParamValue);
  } else {
    url.searchParams.delete(viewParam);
  }
  return url
}

function getCurrentUrlState(): URLState {
  const url = new URL(window.location as any);
  return {
    stack: url.searchParams.get(viewParam) === stackParamValue
  }
}

export function useUrlState() {
  const currentState: URLState = getCurrentUrlState()
  return {
    replaceHistory: (s: URLState) => {
      window.history.replaceState({ "bruh": "aaa" }, '', mergeState(currentState, s));
    },
    state: currentState
  }
}
