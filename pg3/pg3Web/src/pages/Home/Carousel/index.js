import React from 'react';

// Components / Pages
import Slider from 'react-animated-slider';
import { FaChevronLeft, FaChevronRight } from 'react-icons/fa';

// styles
import { SliderCt, Slide, SlideContent, SliderArrow } from './styles';

// config
import { homeConfig } from '../../../config/appConfig';

export default function Carrousel() {
  return (
    <SliderCt>
      <Slider
        autoplay={2000}
        touchDisabled={true}
        previousButton={
          <SliderArrow>
            <FaChevronLeft />
          </SliderArrow>
        }
        nextButton={
          <SliderArrow>
            <FaChevronRight />
          </SliderArrow>
        }
      >
        {homeConfig.carousel.map((data, index) => (
          <Slide key={index} image={data.img}>
            <SlideContent>
              <h2>{data.title}</h2>
              <p>{data.description}</p>
            </SlideContent>
          </Slide>
        ))}
      </Slider>
    </SliderCt>
  );
}
