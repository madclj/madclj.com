
type DFOpts = Intl.DateTimeFormatOptions

function mkFormatter(langs: string[], opts: DFOpts) {
  return Intl.DateTimeFormat(langs, opts);
}

// [...navigator.languages] would be better but own buttons need translations:
const langs = ["en"]
const timeFormat: DFOpts = { timeStyle: 'short', hourCycle: "h23" }
const dateTimeF = mkFormatter(langs, { ...timeFormat, dateStyle: 'medium' })
const yearMonthFormat: DFOpts = { year: 'numeric', month: 'long' }
const yearMonthShortFormat: DFOpts = { month: 'numeric', year: "numeric" }
const formatDateTimeRange = (a: Date, b: Date | undefined) => b
  ? dateTimeF.formatRange(a, b)
  : dateTimeF.format(a)

export default function useDateFormat() {
  return {
    locales: langs,
    timeShortFormat: timeFormat,
    formatDateTime: dateTimeF.format,
    formatDateTimeRange: formatDateTimeRange,
    yearMonthFormat: yearMonthFormat,
    yearMonthShortFormat: yearMonthShortFormat,
  };
}
