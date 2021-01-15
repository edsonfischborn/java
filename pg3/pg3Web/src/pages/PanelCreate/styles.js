import styled from 'styled-components';
import { Form as unForm } from '@rocketseat/unform';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Form = styled(unForm)`
  background-color: ${colorsConfig.bgSecondary};
  border-radius: 5px;
  box-shadow: 0px 0px 15px 2px rgba(0, 0, 0, 0.2);
  padding: 80px;
  width: 100%;

  span {
    color: ${colorsConfig.error};
    font-size: 0.8rem;
  }

  @media only screen and (max-width: 1200px) {
    margin-top: 20px;
    padding: 20px;
  }
`;

export const FormSubmit = styled.div.attrs((props) => ({
  className: 'd-flex justify-center align-center',
}))`
  width: 100%;

  button {
    background-color: ${colorsConfig.primary};
    height: 45px;
    width: 50%;
    border: none;
    color: ${colorsConfig.textLight};
    transition: 0.8s;
    cursor: pointer;
    border-radius: 3px;
  }

  button:hover {
    background-color: ${colorsConfig.secondary};
  }
`;

export const FormGroup = styled.fieldset`
  margin-bottom: 20px;

  h4 {
    margin-bottom: 10px;
    color: ${colorsConfig.primary};
  }

  input + input,
  select + select {
    margin-right: 10px;
  }

  input,
  select {
    margin-right: 10px;
    height: 45px;
  }

  input,
  select,
  textarea {
    background-color: ${colorsConfig.bgSecondary};
    margin-top: 2px;
    border: 1px solid ${colorsConfig.primary};
    padding: 5px;
    border-radius: 3px;
    font-size: 14px;
    resize: vertical;
  }

  @media only screen and (max-width: 1200px) {
    input,
    select {
      margin: 0;
    }
  }
`;

export const MultiData = styled.div.attrs((props) => ({
  className: 'd-flex justify-between align-start',
}))`
  div {
    display: flex;
    flex-direction: column;
    width: 100%;
    margin-bottom: 10px;
  }

  @media only screen and (max-width: 1200px) {
    & {
      flex-direction: column;
    }
  }
`;
