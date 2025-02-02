package request.tdo.constructors;

import db.entity.Account;
import db.entity.UserInfo;
import request.tdo.AccountDTO;
import request.tdo.UserInfoDTO;

public class AccountTDOConstructor {

    // Метод для преобразования сущности UserInfo в DTO
    public UserInfoDTO userInfoToDTO(UserInfo userInfoEntity) {
        if (userInfoEntity == null) {
            return null;
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(userInfoEntity.getUserId());
        userInfoDTO.setAccountId(userInfoEntity.getAccount().getAccountId());
        userInfoDTO.setFirstName(userInfoEntity.getFirstName());
        userInfoDTO.setLastName(userInfoEntity.getLastName());
        userInfoDTO.setEmail(userInfoEntity.getEmail());
        userInfoDTO.setContactInfo(userInfoEntity.getContactInfo());
        return userInfoDTO;
    }

    // Метод для преобразования сущности Account в DTO
    public AccountDTO accountToDTO(Account accountEntity) {
        if (accountEntity == null) {
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountEntity.getAccountId());
        accountDTO.setUsername(accountEntity.getUsername());
        accountDTO.setPasswordHash(accountEntity.getPasswordHash());
        accountDTO.setAccessLevel(accountEntity.getAccessLevel());
        accountDTO.setCreatedAt(accountEntity.getCreatedAt());
        return accountDTO;
    }

    // Метод для преобразования DTO в сущность UserInfo
    public UserInfo dtoToUserInfo(UserInfoDTO userInfoDTO, Account account) {
        if (userInfoDTO == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userInfoDTO.getUserId());
        userInfo.setAccount(account);
        userInfo.setFirstName(userInfoDTO.getFirstName());
        userInfo.setLastName(userInfoDTO.getLastName());
        userInfo.setEmail(userInfoDTO.getEmail());
        userInfo.setContactInfo(userInfoDTO.getContactInfo());
        return userInfo;
    }

    // Метод для преобразования DTO в сущность Account
    public Account dtoToAccount(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }
        Account account = new Account();
        account.setAccountId(accountDTO.getAccountId());
        account.setUsername(accountDTO.getUsername());
        account.setPasswordHash(accountDTO.getPasswordHash());
        account.setAccessLevel(accountDTO.getAccessLevel());
        account.setCreatedAt(accountDTO.getCreatedAt());
        return account;
    }
}
