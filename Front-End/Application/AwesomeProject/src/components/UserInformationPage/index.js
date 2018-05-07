import React , { Component } from 'react'
import { View, TextInput, Dimensions, Image } from 'react-native'
import { Container, Content, Icon, Button, Text, Root } from 'native-base'
import style from './style'
import { Font, AppLoading } from "expo";


class UserInformation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: true,
        }
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
                   <Text></Text>
                </Content>
            </Container>
        );
    }
}

export default UserInformation;
