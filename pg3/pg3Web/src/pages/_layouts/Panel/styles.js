import styled from 'styled-components';

// Config
import { colorsConfig } from '../../../config/appConfig';

export const Main = styled.main`
  min-height: 80vh;

  header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    min-height: 10vh;

    @media only screen and (max-width: 800px) {
      flex-direction: column;
      align-items: center;
      min-height: 15vh;
    }

    h2 {
      color: ${colorsConfig.primary};
    }

    a {
      text-decoration: none;

      button {
        background-color: ${colorsConfig.primary};
        color: ${colorsConfig.textLight};
        display: flex;
        align-items: center;
        padding: 10px;
        cursor: pointer;
        transition: 0.6s;
        text-transform: uppercase;

        svg {
          margin-right: 5px;
          font-size: 1.3em;
        }

        &:hover {
          background-color: ${colorsConfig.secondary};
        }
      }
    }
  }
`;
