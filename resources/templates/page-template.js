import React from 'react';
import PropTypes from 'prop-types';
import $$layout$$ from './layouts/$$layout$$';
import {View} from 'react-native';

const $$page-name$$ = props => {
    return (
        <$$layout$$>
            <Text>$$page-name$$</Text>
        </$$layout$$>
    );
};

$$page-name$$.propTypes = {
    navigation: PropTypes.object,
};

export default $$page-name$$;
