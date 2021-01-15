import { createGlobalStyle } from 'styled-components';

import 'react-toastify/dist/ReactToastify.css';
import 'react-animated-slider/build/horizontal.css';
import 'animate.css';

// Config
import { colorsConfig } from '../config/appConfig';

export const GlobalStyle = createGlobalStyle`
  :root {
    --animate-delay: 0.5s;
  }

* {
    margin: 0;
    outline: 0;
    padding: 0;
    border: 0;
    box-sizing: border-box;
  }

  body {
    font-family: 'Inter', sans-serif;
    font-size: 16px;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    background-color: ${colorsConfig.bgPrimary};
    color: ${colorsConfig.textDark};
    letter-spacing: 1.4px;
    line-height: 26px;
  }

  div#root, body {
    height: 100vh;
  }

  input, textarea, button, select, select::placeholder, input::placeholder, textarea::placeholder{
    font-family: 'Inter', sans-serif;
    color: ${colorsConfig.textDark};
    letter-spacing: 1.4px;
    line-height: 26px;
    font-size: 1rem;
  }

  input::placeholder, textarea::placeholder, select::placeholder {
    color: ${colorsConfig.textDark};
    opacity: 0.6;
    font-size: 0.9rem;
  }

  input, textarea, button {
    border-radius: 2px;
  }

  input, textarea {
    padding: 5px 10px;
  }

  /* Class */
  .leaflet-container {
    height: 280px;
    width: 300px;
  }

  .d-flex {
    display: flex;
  }

  .align-center{
    align-items: center;
  }

  .align-start {
    align-items: flex-start;
  }

  .justify-around {
    justify-content: space-around;
  }

  .justify-center {
    justify-content: center;
  }

  .justify-between {
    justify-content: space-between;
  }

  .row {
    flex-direction: row;
  }

  .column {
    flex-direction: column;
  }

  .tx-c{
    text-align: center;
  }

  @media only screen and (max-width: 1200px) {
    .align-start {
      align-items: center;
    }
  }
`;
