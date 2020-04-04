// LegalStack
createStackNavigator({
	PrivacyPolicy,
});

// LoginStack
createStackNavigator({
	LoginPage,
	RegisterPage,
	LegalStack,
});

// RootStack
createStackNavigator({
	HomePage,
	SettingsPage,
	LoginStack,
});

