import styled from 'styled-components';

import Loader from 'react-loader-spinner';

// Config
import { colorsConfig } from '../../config/appConfig';

const defaultHeight = '25%';
const defaultColor = colorsConfig.primary;
const defaultType = 'Oval';

export default styled(Loader).attrs((props) => ({
  type: props.type || defaultType,
  color: props.color || defaultColor,
  height: props.height || defaultHeight,
}))`
  height: 100%;
  display: flex;
  align-items: center;
`;
