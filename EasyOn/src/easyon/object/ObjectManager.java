package easyon.object;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import easyon.object.model.User;

/** ��� ������Ʈ�� �����ϴ� Ŭ���� **/
public class ObjectManager {
    private static final ObjectManager instance = new ObjectManager();

    public static ObjectManager getInstance() {
        return instance;
    }

    private Map<Integer, User> _allUsers; // ��� ���� ���� key:user.no, value:user
    private Map<Integer, User> _loginUsers; // �α��� ���� ���� key:user.no, value:user

    private ObjectManager() {
        _allUsers = new HashMap<Integer, User>();
        _loginUsers = new ConcurrentHashMap<Integer, User>();
    }

    /** ���� ���
     *  @param user ���� **/
    public void addUser(User user) {
        if (!_allUsers.containsKey(user.getNo())) {
            _allUsers.put(user.getNo(), user);
        }
    }

    /** �ش� ��ȣ�� ���� ��ȯ
     *  @param no ���� ��ȣ **/
    public User getUser(int no) {
        if (_allUsers.containsKey(no)) {
            return _allUsers.get(no);
        }
        return null;
    }

    /** �ش� ���̵��� ���� ��ȯ
     *  @param id ���� ���̵� **/
    public User getUser(String id) {
        for (User user : _allUsers.values()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /** �α��� ������ Ȯ���� ��ġ�� ���� ��ȯ
     *  @param id ���̵�
     *  @param password ��й�ȣ
     *  @return �α��� ����:User, ����:null **/
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

    /** �α��ε� ���� �α׾ƿ� ó��
     *  @param no ���� ��ȣ **/
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
