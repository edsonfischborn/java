import styled from 'styled-components';

// Config
import { colorsConfig } from '../../config/appConfig';

export const Badges = styled.div.attrs(() => ({
  className: 'd-flex',
}))`
  align-self: flex-end;

  div {
    display: flex;
    justify-content: center;
    align-items: center;

    span {
      margin: 0 10px 0 5px;
    }
  }

  @media only screen and (max-width: 1200px) {
    align-self: center;
    margin: 10px 0;
    flex-direction: column;
  }
`;

export const TableCt = styled.section`
  overflow-y: hidden;
  overflow-x: auto;

  &::-webkit-scrollbar {
    height: 5px;
    background-color: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: ${colorsConfig.primary};
    border-radius: 3px;
  }
`;

export const Table = styled.table`
  text-align: center;
  border-collapse: separate;
  border-spacing: 0 20px;
  width: 100%;

  thead {
    th {
      padding: 0px 10px;
    }
  }

  tbody {
    tr {
      background-color: ${colorsConfig.bgSecondary};
      border-radius: 5px;
      box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.2);

      td {
        padding: 20px 10px;

        div {
          margin: 0 auto;
        }

        a {
          color: ${colorsConfig.primary};
          background-color: transparent;
          cursor: pointer;
          font-size: 1.2em;
        }
      }
    }
  }
`;

export const InfoContainer = styled.div.attrs(() => ({
  className: 'd-flex align-center justify-center',
}))`
  padding: 50px 0;

  span {
    display: block;
  }
`;

export const HandlePage = styled.div.attrs(() => ({
  className: 'd-flex align-center justify-around',
}))`
  margin-top: 5px;

  button {
    background: none;
    display: flex;
    align-items: center;
    cursor: pointer;

    svg {
      color: ${colorsConfig.primary};
      margin: 0px 3px;
    }
  }
`;
