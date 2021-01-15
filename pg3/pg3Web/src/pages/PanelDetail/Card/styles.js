import styled from 'styled-components';

// Config
import { colorsConfig } from '../../../config/appConfig';

export const Ct = styled.div.attrs((props) => ({
  className: 'd-flex align-start column',
}))`
  min-height: 30vh;
  width: 30% !important;
  background-color: ${colorsConfig.bgSecondary};
  border-radius: 5px;
  padding: 20px;
  box-shadow: 0px 0px 15px 2px rgba(0, 0, 0, 0.2);
  transition: 0.5s;
  overflow: hidden;

  svg {
    font-size: 50px;
    color: ${colorsConfig.primary};
    margin-bottom: 5px;
    transition: 0.5s;
  }

  &:hover {
    box-shadow: 0px 0px 30px 3px rgba(0, 0, 0, 0.5);

    svg {
      color: ${colorsConfig.secondary};
    }
  }

  @media only screen and (max-width: 1200px) {
    & {
      width: 100% !important;
    }
  }
`;

export const Header = styled.div.attrs((props) => ({
  className: 'd-flex align-center justify-center column',
}))`
  width: 100%;
`;

export const Body = styled.div`
  padding-top: 20px;
  width: 100%;

  div {
    margin-bottom: 5px !important;
    -ms-word-break: break-word;
    word-break: break-word;
    white-space: pre-wrap;

    p {
      letter-spacing: 0;
      display: inline;
    }

    strong {
      margin-right: 5px;

      &:after {
        content: ':';
      }
    }
  }
`;
