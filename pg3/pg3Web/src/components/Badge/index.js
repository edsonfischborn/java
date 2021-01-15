import styled from 'styled-components';

const defaultSize = '1em';
const defaultColor = '#bb2124';

export default styled.div`
  border-radius: 50%;
  width: ${(props) => props.size || defaultSize};
  height: ${(props) => props.size || defaultSize};
  background-color: ${(props) => props.color || defaultColor};
`;
