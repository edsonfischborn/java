import styled from 'styled-components';

// Config
import { colorsConfig } from '../../../config/appConfig';

export const Container = styled.main.attrs(() => ({
  className: 'd-flex align-center justify-center',
}))`
  background-image: url(${(props) => props.bgImg});
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  box-shadow: inset 100vw 100vh 0px 200px rgba(0, 0, 0, 0.3);
  min-height: 100vh;
  padding: 20px 0px;

  form {
    min-height: 50vh;
    min-width: 30vw;
    width: 450px;
    background-color: ${colorsConfig.primary};
    opacity: 0.9;
    padding: 30px;
    border-radius: 5px;
    box-shadow: 1px 1px 10px 1px rgba(0, 0, 0, 0.6);

    label,
    h2,
    button {
      color: ${colorsConfig.textLight};
    }

    h2,
    button {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    h2 {
      font-size: 1.8rem;
      margin-bottom: 20px;

      svg {
        margin-right: 5px;
      }
    }

    input {
      margin-bottom: 20px;
      width: 100%;
      height: 45px;
      background: ${colorsConfig.bgSecondary};
    }

    span,
    label {
      display: block;
    }

    span {
      margin-top: -15px;
      color: ${colorsConfig.error};
    }

    button {
      height: 50px;
      width: 100%;
      background-color: ${colorsConfig.secondary};
      cursor: pointer;
      transition: 0.8s;
      margin-bottom: 10px;
      font-weight: bold;

      & > svg {
        font-size: 1.2rem;
        margin-left: 5px;
      }

      &:hover {
        opacity: 0.8;
      }
    }

    p {
      color: ${colorsConfig.textLight};

      a {
        color: ${colorsConfig.textLight};
      }
    }

    @media only screen and (max-width: 1200px) {
      width: 90%;
    }
  }
`;
