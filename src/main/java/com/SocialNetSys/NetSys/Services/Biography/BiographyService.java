package com.SocialNetSys.NetSys.Services.Biography;

import com.SocialNetSys.NetSys.Models.Objects.Biography_Model;
import com.SocialNetSys.NetSys.Models.Requests.BiographyRequest;
import com.SocialNetSys.NetSys.Services.User.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.LinkedList;
import java.util.UUID;

@Service
public class BiographyService implements IBiographyService {
    @Autowired
    private IUserService _userService;

    public LinkedList<Biography_Model> createBiography(BiographyRequest request, HttpServletRequest servletRequest) {

        var userIdFromRequest = (String) servletRequest.getAttribute("user_id");
        UUID userId = UUID.fromString(userIdFromRequest);

        var user = _userService.getUserByID(userId);


        try{
            for(Biography_Model b : request.biography) {
                var bio = new Biography_Model();

                bio.setType(b.getType());
                bio.setValue(b.getValue());
                bio.setUser_id(userId);

                user.setBiography(bio);

                _userService.updateUserInDB(user);
            }
        } catch(Exception e) {
            throw new RuntimeException("Failed to save biography for user");
        }

        return user.getBiography();
    };

    public LinkedList<Biography_Model> updateBiography(UUID itemBioId, Biography_Model request, HttpServletRequest servletRequest) {

        var userIdFromRequest = (String) servletRequest.getAttribute("user_id");
            UUID userId = UUID.fromString(userIdFromRequest);
            var user = _userService.getUserByID(userId);
            var biography = user.getBiography();

            try {
                for (int i = 0; i < biography.size(); i++) {
                    Biography_Model bio = biography.get(i);

                    if (itemBioId.equals(bio.getId())) {
                        var newBio = new Biography_Model();

                        newBio.setId(itemBioId);
                        newBio.setType(request.getType());
                        newBio.setValue(request.getValue());
                        newBio.setUser_id(userId);

                        biography.set(i, newBio);

                        user.replaceBiography(biography);

                        _userService.updateUserInDB(user);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException("Failed to update biography for user");
            }

            return user.getBiography() ;

    }

    public void deleteBiography(UUID itemBioId, HttpServletRequest servletRequest) {

        var userIdFromRequest = (String) servletRequest.getAttribute("user_id");
        UUID userId = UUID.fromString(userIdFromRequest);

        var user = _userService.getUserByID(userId);
        var biography = user.getBiography();

        if(biography == null) {
            throw new RuntimeException("Biografia não encontrada");
        }

        try {
            for(Biography_Model bio : biography) {
                if(bio.getId().equals(itemBioId)) {
                    biography.remove(bio);
                    user.replaceBiography(biography);
                }
            }
            _userService.updateUserInDB(user);

        } catch(Exception e) {
            throw new RuntimeException("Não foi possível deletar a biografia");
        }

    }
}
