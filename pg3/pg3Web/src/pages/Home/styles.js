import styled from 'styled-components';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Header = styled.header`
  position: relative;
`;

export const JumpButton = styled.button.attrs((props) => ({
  className: 'd-flex align-center justify-center',
}))`
  position: absolute;
  width: 200px;
  height: 45px;
  bottom: 20%;
  left: calc(50% - 200px / 2);
  font-size: 20px;
  background-color: ${colorsConfig.primary};
  transition: 0.6s;
  cursor: pointer;

  &:hover {
    background-color: ${colorsConfig.secondary};
  }

  a {
    width: 100%;
    color: ${colorsConfig.textLight};
    text-decoration: none;
    margin-right: 10px;
  }
`;

export const Main = styled.main`
  h2 {
    color: ${colorsConfig.primary};
  }
`;

// Contact
export const ContactContainer = styled.div.attrs(() => ({
  className: 'd-flex align-start justify-between',
}))`
  width: 100%;

  h4 {
    font-size: 1.1rem;
    margin: 5px 0;
  }

  @media only screen and (max-width: 1200px) {
    flex-direction: column;
    min-height: 830px;

    h4 {
      margin: 10px 0;
    }

    & > ul,
    div {
      align-self: stretch;
    }
  }
`;

// Contact - phone/email
export const ContactOptions = styled.ul`
  list-style: none;

  li {
    height: 60px;
    display: flex;
    align-items: center;

    svg {
      font-size: 1.1rem;
      color: ${colorsConfig.primary};
      margin-right: 5px;
    }
  }
`;
