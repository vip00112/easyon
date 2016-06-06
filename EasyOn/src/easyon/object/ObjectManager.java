package easyon.object;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import easyon.object.model.User;

/** 모든 오브젝트를 관리하는 클래스 **/
public class ObjectManager {
    private static final ObjectManager instance = new ObjectManager();

    public static ObjectManager getInstance() {
        return instance;
    }

    private Map<Integer, User> _allUsers; // 모든 유저 정보 key:user.no, value:user
    private Map<Integer, User> _loginUsers; // 로그인 유저 정보 key:user.no, value:user

    private ObjectManager() {
        _allUsers = new HashMap<Integer, User>();
        _loginUsers = new ConcurrentHashMap<Integer, User>();
    }

    /** 유저 등록
     *  @param user 유저 **/
    public void addUser(User user) {
        if (!_allUsers.containsKey(user.getNo())) {
            _allUsers.put(user.getNo(), user);
        }
    }

    /** 해당 번호의 유저 반환
     *  @param no 유저 번호 **/
    public User getUser(int no) {
        if (_allUsers.containsKey(no)) {
            return _allUsers.get(no);
        }
        return null;
    }

    /** 해당 아이디의 유저 반환
     *  @param id 유저 아이디 **/
    public User getUser(String id) {
        for (User user : _allUsers.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /** 로그인 정보를 확인후 일치시 유저 반환
     *  @param id 아이디
     *  @param password 비밀번호
     *  @return 로그인 성공:User, 실패:null **/
    public User getUserByLogin(String id, String password) {
        for (User user : _allUsers.values()) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                if (!_loginUsers.containsKey(user.getNo())) {
                    _loginUsers.put(user.getNo(), user);
                }
                return user;
            }
        }
        return null;
    }

    /** 로그인된 유저 로그아웃 처리
     *  @param no 유저 번호 **/
    public void removeLoginUser(int no) {
        if (_loginUsers.containsKey(no)) {
            _loginUsers.remove(no);
            User user = getUser(no);
            if (user != null) {
                user.logout();
            }
        }
    }

}
