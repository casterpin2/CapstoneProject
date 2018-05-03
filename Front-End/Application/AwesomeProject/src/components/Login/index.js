import React , { Component } from 'react'
import { View, TextInput, Dimensions, Image } from 'react-native'
import { Container, Content, Icon, Button, Text, Root } from 'native-base'
import style from './style'
import { Font, AppLoading } from "expo";


class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            usernameStr : '',
            passwordStr : '',
            errorRequiredMessage : ''
        }
        this._setErrorRequiredMessage = this._setErrorRequiredMessage.bind(this);
        this._stepToHomepage = this._stepToHomepage.bind(this);
      }
      

      async componentWillMount() {
        await Font.loadAsync({
          Roboto: require("native-base/Fonts/Roboto.ttf"),
          Roboto_medium: require("native-base/Fonts/Roboto_medium.ttf")
        });
        this.setState({ loading: false });
      }

      _setErrorRequiredMessage = (errorRequiredMessage) => {
        this.setState({
            errorRequiredMessage : 'Username and Password are required! Try again..'
        });
      }
      _stepToHomepage = () => {
        this.props.navigation.navigate('homepage');
      }

      static navigationOptions = () => {
        return {
            header : null,
        }
      }



    render(){
        if (this.state.loading) {
            return (
              <Root>
                <AppLoading />
              </Root>
            );
          }
        return (
            <Container>
                <Content>
                    <Image
                        style={{
                            flex: 1,
                            position: 'absolute',
                            width: '100%',
                            height: '100%',
                            justifyContent: 'center',
                        }}
                        source={require('../../../resources/images/login_background.jpg')}
                    />
                    <View style = {style.loginPage}>
                        <View style = {style.logo}><Image source={require('../../../resources/images/grab_logo.png')} /></View>
                        <View style= {style.loginForm}>
                            <View style = {style.inputRow}>
                                <Icon name='md-person' style = {style.icon}/>
                                <TextInput 
                                    placeholder = 'Enter Username'
                                    style = {style.textInput}
                                    returnKeyType = {"next"}
                                    onChangeText = {(usernameStr) => {this.setState({ usernameStr})}}
                                    value = {this.state.usernameStr}
                                />
                            </View>
                            <View style = {style.inputRow}>
                                <Icon name='ios-pricetags' style = {style.icon}/>
                                <TextInput secureTextEntry
                                    placeholder = 'Enter Password'
                                    style = {style.textInput}
                                    returnKeyType = {"next"}
                                    onChangeText = {(passwordStr) => {this.setState({ passwordStr})}}
                                    value = {this.state.passwordStr}
                                />
                            </View>
                            <View style= {style.buttonView}>
                                { this.state.usernameStr && this.state.passwordStr
                                    ? <Button block transparent style = {style.loginBtn} onPress = {this._stepToHomepage}>
                                    <Text>Login</Text>
                                    </Button>
                                    : <Button block transparent style = {style.loginBtn} onPress = {this._setErrorRequiredMessage}>
                                    <Text>Login</Text>
                                    </Button>
                                }                                
                            </View>
                            {this.state.usernameStr && this.state.passwordStr
                            ? null
                            : <View>
                                <Text style = {style.errorMessage}>{this.state.errorRequiredMessage}</Text>
                              </View>
                            }
                        </View>

                        <View style = {style.loginWithFBandGGForm}>
                            <Button block success style = {style.loginFBBtn} >
                                <Text>Login via Facebook</Text>
                            </Button>
                            <Button block success style = {style.loginGGBtn} >
                                <Text>Login via Google</Text>
                                </Button>
                        </View>
                    </View> 
                </Content>
            </Container>
        );
    }
}

export default Login;
