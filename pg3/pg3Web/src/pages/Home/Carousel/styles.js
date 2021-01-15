import styled from 'styled-components';

// Config
import { colorsConfig } from '../../../config/appConfig';

export const SliderCt = styled.section`
  .slider {
    height: 90vh;
    color: ${colorsConfig.textLight};

    svg {
      color: ${colorsConfig.textLight};
    }

    @media only screen and (max-width: 1200px) {
      svg {
        display: none !important;
      }
    }
  }
`;

export const Slide = styled.div`
  background-image: url(${(props) => props.image});
  background-size: cover;
  background-position: center;
  box-shadow: inset 100vw 100vh 0px 200px rgba(0, 0, 0, 0.5);
`;

export const SlideContent = styled.div.attrs(() => ({
  className: 'd-flex align-center justify-center column',
}))`
  width: 100%;
  height: 100%;

  p {
    font-size: 18px;
  }

  @media only screen and (max-width: 1200px) {
    padding: 0 15px;

    svg {
      display: none !important;
    }
  }
`;

export const SliderArrow = styled.div`
  font-size: 30px;
`;
