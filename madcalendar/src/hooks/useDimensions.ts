// Source: https://stackoverflow.com/a/36862446
import { useState, useEffect } from 'react';

function getWindowDimensions() {
  const { innerWidth: width, innerHeight: height } = window;
  return {
    width,
    height
  };
}

export default function useWindowDimensions() {
  const [windowDimensions, setWindowDimensions] = useState(getWindowDimensions());

  useEffect(() => {
    function handleResize() {
      setWindowDimensions(getWindowDimensions());
    }

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return windowDimensions;
}

export function useGrid() {
  const dimensions = useWindowDimensions()
  const wRatio = dimensions.width / dimensions.height
  return {
    atLeastVerySmall: dimensions.width > 400,
    atLeastSmall: dimensions.width > 600,
    atLeastMedium: dimensions.width > 900,
    atLeastLarge: dimensions.width > 1200,
    whRatioAtLeastSquare: wRatio >= 1,
    whRatioAtLeast1W: wRatio >= 1.1,
    whRatioAtLeast2W: wRatio >= 1.2,
    whRatioAtLeast3W: wRatio >= 1.3,
    whRatioAtLeast4W: wRatio >= 1.4,
    whRatioAtLeast5W: wRatio >= 1.5,
    whRatioAtLeast6W: wRatio >= 1.6,
    whRatioAtLeast7W: wRatio >= 1.7,
  }
}
