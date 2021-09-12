package com.example.doan.Controller.api;

import com.example.doan.Entity.Districts;
import com.example.doan.Entity.Provinces;
import com.example.doan.Entity.User;
import com.example.doan.Entity.Wards;
import com.example.doan.Repository.DistrictsRepository;
import com.example.doan.Repository.ProvincesRepository;
import com.example.doan.Repository.WardsRepository;
import com.example.doan.Service.UserService;
import com.example.doan.model.DistrictsDTO;
import com.example.doan.model.WardsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AddressRestController {
    @Autowired
    private DistrictsRepository districtsRepository;

    @Autowired
    private ProvincesRepository provincesRepository;

    @Autowired
    private WardsRepository wardsRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/cart/check-out/provinces/{id}/districts")
    public List<DistrictsDTO> listDistrictsByProvinces(@PathVariable(name = "id") Long provincesId) {
        List<DistrictsDTO> districtsList = new ArrayList<>();

        Provinces provinces = provincesRepository.getById(provincesId);

        List<Districts> districts = provinces.getDistrictList().stream().collect(Collectors.toList());
        for (Districts district : districts) {
            DistrictsDTO dto = new DistrictsDTO(district.getId(), district.getName());
            districtsList.add(dto);
        }
        System.out.println(districtsList);
        return districtsList;
    }

    @GetMapping("/cart/check-out/provinces/{provincesId}/districts/{districtsId}/wards")
    public List<WardsDTO> listDistrictsByProvinces(@PathVariable(name = "districtsId") Long districtsId, @PathVariable String provincesId) {
        List<WardsDTO> wardsDTOList = new ArrayList<>();

        Districts districts = districtsRepository.getById(districtsId);

        List<Wards> wards = districts.getWardList().stream().collect(Collectors.toList());
        for (Wards ward : wards) {
            WardsDTO dto = new WardsDTO(ward.getId(), ward.getName(), ward.getTransport_fee());
            wardsDTOList.add(dto);
        }
        return wardsDTOList;
    }

    @GetMapping("/cart/check-out/wards/{wardsId}/transport-fee")
    public WardsDTO transportFee(@PathVariable(name = "wardsId") Long wardsId) {
        Wards wards = wardsRepository.findById(wardsId).get();
        WardsDTO wardsDTO = new WardsDTO(wards.getId(), wards.getName(), wards.getTransport_fee());
        return wardsDTO;
    }

    @GetMapping("/cart/check-out/total")
    public double getTotal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
        double total = user.getCartItems().stream().mapToDouble(cartItem -> cartItem.getSubtotal()).sum();
        System.out.println(total);
        return total;
    }


}
