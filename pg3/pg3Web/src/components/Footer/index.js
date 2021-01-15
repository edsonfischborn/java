import React from 'react';

// Components / Pages
import { Footer as FooterCt } from './styles';

export default class Footer extends React.Component {
  render() {
    return (
      <FooterCt>
        <span>PGIII @{new Date().getFullYear()}</span>
      </FooterCt>
    );
  }
}
