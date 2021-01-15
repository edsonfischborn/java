import styled from 'styled-components';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Nav = styled.nav.attrs((props) => ({
  className: 'd-flex align-center justify-between',
}))`
  background-color: ${colorsConfig.secondary};
  padding: 0 20px;
  height: 70px;
  color: ${colorsConfig.textLight};

  a {
    color: ${colorsConfig.textLight};
    text-decoration: none;
  }

  a:hover {
    text-decoration: underline;
  }
`;
