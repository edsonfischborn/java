import styled from 'styled-components';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Cards = styled.section.attrs((props) => ({
  className: 'd-flex justify-between align-start',
}))`
  width: 100%;

  @media only screen and (max-width: 1200px) {
    padding: 30px 0px;
    flex-direction: column;
    > div {
      margin-bottom: 20px;
    }
  }
`;

export const PdfViewer = styled.section.attrs((props) => ({
  className: 'd-flex column',
}))`
  width: 100%;
  margin-top: 20px;

  h3,
  svg {
    color: ${colorsConfig.primary};
  }

  svg {
    margin-right: 5px;
  }
`;

export const LoaderCt = styled.div.attrs(() => ({
  className: 'd-flex align-center justify-center',
}))`
  min-height: 50vh;
`;
