import styled from 'styled-components';

// Assets
import bg from '../../assets/bg-login.jpg';

export const Container = styled.main.attrs(() => ({
  className: 'd-flex column align-center justify-center',
}))`
  height: 80vh;
  background-image: url(${bg});
  background-repeat: no-repeat;
  background-size: cover;
  background-position: bottom;
  box-shadow: inset 100vw 80vh 1px rgba(0, 0, 0, 0.5);
  color: #fff;

  h1 {
    margin-bottom: 7px;
  }

  a {
    color: inherit;
  }
`;
