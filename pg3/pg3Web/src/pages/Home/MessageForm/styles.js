import styled from 'styled-components';
import { Form as Unform } from '@rocketseat/unform';

// Config
import { colorsConfig } from '../../../config/appConfig';

export const Form = styled(Unform)`
  width: 100%;

  label {
    display: block;
  }

  input,
  textarea {
    background-color: ${colorsConfig.bgPrimary};
    width: 100%;
    border: 1px solid ${colorsConfig.primary};
    margin-bottom: 5px;
  }

  textarea {
    resize: vertical;
    height: 88px;
  }

  input {
    height: 40px;
  }

  span {
    color: ${colorsConfig.error};
    font-size: 0.8rem;
    margin-top: -8px;
    display: block;
  }

  button {
    background-color: ${colorsConfig.primary};
    height: 40px;
    width: 50%;
    border: none;
    color: #fff;
    transition: 0.8s;
    cursor: pointer;
    float: right;
  }

  input:hover,
  textarea:hover {
    border: 1px solid ${colorsConfig.secondary};
  }

  button:hover {
    background-color: ${colorsConfig.secondary};
  }

  @media only screen and (min-width: 1200px) {
    & {
      width: 40%;
    }
  }
`;
