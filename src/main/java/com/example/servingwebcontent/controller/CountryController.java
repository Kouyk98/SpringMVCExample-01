package com.example.servingwebcontent.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.servingwebcontent.entity.CountryEntity;
import com.example.servingwebcontent.form.CountryForm;
import com.example.servingwebcontent.form.CountrySearchForm;
import com.example.servingwebcontent.repository.CountryEntityMapper;
import com.example.servingwebcontent.utils.CommonResult;
import com.google.gson.Gson;

@Controller
public class CountryController {

	@Autowired
	private CountryEntityMapper mapper;
	
	@Autowired
	private CommonResult result;
	
	@Autowired
	private Gson gson;

	@GetMapping("/country")
	public String init(Model model) {

		model.addAttribute("searchForm", new CountrySearchForm());
		model.addAttribute("countryForm", new CountryForm());
		return "country/country";
	}

	/**
	 * Represents a sequence of characters. In this context, it is used to return a
	 * JSON representation of a CountryEntity object.
	 */
	@PostMapping("/country/getCountry")
	@ResponseBody
	public String getCountry(@Validated CountrySearchForm countrySearchForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		/**
		 * Optional object containing the result of the database query for the country
		 * with the specified country code.
		 */
		Optional<CountryEntity> countryEntity = mapper.selectByPrimaryKey(countrySearchForm.getMstCountryCD());
		if (countryEntity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new Gson().toJson(countryEntity.get());
	}

	/*
	 * 创建一个方法，监听/country/createCountry，
	 * 实现根据请求的参数创建一个CountryEntity对象，并将其插入到数据库中。
	 */
	@PostMapping("/country/create")
	@ResponseBody
	public String create(CountryForm countryForm) {
		// Method body goes here
		CountryEntity countryEntity = new CountryEntity();
		//将countryForm中的数据传给entity
		countryEntity.setMstcountrycd(countryForm.getCd());
		countryEntity.setMstcountrynanme(countryForm. getName());

		
		// you might save the countryEntity to the database
		mapper.insert(countryEntity);
		result.setStatus(0);
		result.setMessage("数据加入成功");
		// Then return a success message or the saved entity
		return gson.toJson(result);
	}
	@PostMapping("/country/update")
	@ResponseBody
	public String update(CountryForm countryForm) {
		// Method body goes here
		CountryEntity countryEntity = new CountryEntity();
		//将countryForm中的数据传给entity
		countryEntity.setMstcountrycd(countryForm.getCd());
		countryEntity.setMstcountrynanme(countryForm. getName());
		
		// you might save the countryEntity to the database
		mapper.updateByPrimaryKey(countryEntity);
		// Then return a success message or the saved entity
		result.setStatus(0);
		result.setMessage("数据更新成功");
		return gson.toJson(result);
	}
	
	@PostMapping("/country/delete")
	@ResponseBody
	public String delete(CountryForm countryForm) {
		// Method body goes here
		
		// you might save the countryEntity to the database
		mapper.deleteByPrimaryKey(countryForm.getCd());
		// Then return a success message or the saved entity
		result.setStatus(0);
		result.setMessage("数据删除成功");
		return gson.toJson(result);
	}

}
