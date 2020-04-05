import React from 'react';
import {View, Text, Button} from 'react-native';
import PropTypes from 'prop-types';
import Routes from '../routes';
import $$layout$$ from './layouts/$$layout$$';

const $$page-name$$ = props => {
    return (
        <$$layout$$>
            <Text>$$page-name$$</Text>
            $$buttons$$
        </$$layout$$>
    );
};

$$page-name$$.propTypes = {
    navigation: PropTypes.object,
};

export default $$page-name$$;
