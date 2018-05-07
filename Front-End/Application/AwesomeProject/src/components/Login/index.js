import React , { Component } from 'react'
import { View, TextInput, Dimensions, Image, TouchableOpacity } from 'react-native'
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

    //   componentDidMount() {

    //   }

    //   _onPress = async () => {
    //       try {
    //             const data = new FormData();
    //             const response = fetch('url', data)
    //             const method = 'POST'

    //             data.append('username', username)
    //             data.append('password', password)
    //       }
    //       catch (error) {

    //       }
    //   }
      
    //   _handleNetwork(response, error) {

    //   }

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
                                    placeholder = 'Nhập tên đăng nhập...'
                                    style = {style.textInput}
                                    returnKeyType = {"next"}
                                    onChangeText = {(usernameStr) => {this.setState({ usernameStr})}}
                                    value = {this.state.usernameStr}
                                />
                            </View>
                            <View style = {style.inputRow}>
                                <Icon name='ios-pricetags' style = {style.icon}/>
                                <TextInput secureTextEntry
                                    placeholder = 'Nhập mật khẩu...'
                                    style = {style.textInput}
                                    returnKeyType = {"next"}
                                    onChangeText = {(passwordStr) => {this.setState({ passwordStr})}}
                                    value = {this.state.passwordStr}
                                />
                            </View>
                            <View style= {style.buttonView}>
                                { this.state.usernameStr && this.state.passwordStr
                                    ? <Button block style = {style.loginBtn} onPress = {this._stepToHomepage}>
                                    <Text>Đăng nhập</Text>
                                    </Button>
                                    : <Button block style = {style.loginBtn} onPress = {this._setErrorRequiredMessage}>
                                    <Text>Đăng nhập</Text>
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
                            <Text style = {{color: 'white', marginBottom: 10}}>hoặc </Text>
                            <TouchableOpacity style={style.FacebookStyle} activeOpacity={0.5}>
                                <Image 
                                    source={require('../../../resources/images/fb_logo.png')} 
                                    style={style.ImageIconStyle} 
                                />
                                <View style={style.SeparatorLine} />
                                <Text style={style.TextStyle}> Đăng nhập với tài khoản Facebook </Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={style.GooglePlusStyle} activeOpacity={0.5}>
                                <Image 
                                    source={require('../../../resources/images/gg_logo.png')} 
                                    style={style.ImageIconStyle} 
                                />
                                <View style={style.SeparatorLine} />
                                <Text style={style.TextStyle}> Đăng nhập với tài khoản Google Plus </Text>
                            </TouchableOpacity>
                        </View>
                    </View> 
                </Content>
            </Container>
        );
    }
}


export default Login;

 
 