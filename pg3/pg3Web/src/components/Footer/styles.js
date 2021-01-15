import styled from 'styled-components';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Footer = styled.footer.attrs((props) => ({
  className: 'd-flex align-center justify-center',
}))`
  background-color: ${colorsConfig.secondary};
  height: 70px;

  span {
    color: ${colorsConfig.textLight};
  }
`;
