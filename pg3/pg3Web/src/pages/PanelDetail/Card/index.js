import React from 'react';

// Components / Pages
import { Ct as CardCt, Header, Body } from './styles';

export default function Card({ children, icon, title }) {
  return (
    <CardCt>
      <Header>
        {icon}
        <h3>{title}</h3>
      </Header>
      <Body>{children}</Body>
    </CardCt>
  );
}
