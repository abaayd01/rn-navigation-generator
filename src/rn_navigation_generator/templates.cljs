(ns rn-navigation-generator.templates)

(def page-template 
"import React from 'react';
import {View, Text, Button} from 'react-native';
import PropTypes from 'prop-types';
import Routes from '../routes';
import {{layout}} from './layouts/{{layout}}';

const {{page-name}} = props => {
    return (
        <{{layout}}>
            <Text>{{page-name}}</Text>
            {{#each route-names}}
            <Button onPress={() => props.navigation.navigate(Routes.{{this}})}>
                {{this}}
            </Button>
            {{/each}}
        </{{layout}}>
    );
};

{{page-name}}.propTypes = {
    navigation: PropTypes.object,
};

export default {{page-name}};
")

(def root-navigator-template 
"{{#each stacks}}
{{#each route-names}}
import {{this}} from '../pages/{{this}}';
{{/each}}
{{/each}}

{{#each stacks}}
const {{{stack-name}}} = createStackNavigator(
    {
        {{#each route-names}}
        {{this}},
        {{/each}}
    },
);
{{/each}}
")

(def routes-template 
"const routes = {
    {{#each route-names}}
    {{this}}: '{{this}}',
    {{/each}}
};

export default routes;")

