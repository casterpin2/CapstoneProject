import { StackNavigator } from 'react-navigation'
import Login from './components/Login/index'
import Homepage from './components/Homepage/index'
import SearchPage from './components/SearchPage/index'

const AppRouter = StackNavigator({
    login: {
        screen: Login
    },
    searchpage: {
        screen: SearchPage
    },
    homepage : {
        screen : Homepage
    }
}, {
    initialRouteName: 'homepage',
})

export default AppRouter;