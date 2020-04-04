import React from 'react';
import PropTypes from 'prop-types';
import BaseLayout from './layouts/BaseLayout';
import {View} from 'react-native';

const HomePage = props => {
    return (
        <BaseLayout>
            <Text>HomePage</Text>
        </BaseLayout>
    );
};

HomePage.propTypes = {
    navigation: PropTypes.object,
};

export default HomePage