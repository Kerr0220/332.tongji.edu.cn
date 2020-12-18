package com.learn.demo.springbootdemo.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import com.learn.demo.springbootdemo.util.EasyPoiUtil;
import com.learn.demo.springbootdemo.pojo.Score;
import com.learn.demo.springbootdemo.service.ScoreService;
import org.springframework.web.multipart.MultipartFile;
import com.learn.demo.springbootdemo.common.PageResult;
import com.learn.demo.springbootdemo.common.Result;
import com.learn.demo.springbootdemo.common.StatusCode;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @description: 控制器层
 * @author chenbin
 * @version: V1.0
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/score")
public class ScoreController {

	@Autowired
	private ScoreService scoreService;


	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping()
	public Result findAll(){
		return Result.SUCCESS("查询成功",scoreService.findAll());
	}

	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping(value="/{id}")
	public Result findById(@PathVariable String id){
		return Result.SUCCESS("查询成功",scoreService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping(value="/search/{page}/{size}")
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Score> pageList = scoreService.findSearch(searchMap, page, size);
		return  Result.SUCCESS("查询成功",  new PageResult<Score>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value="/search")
    public Result findSearch( @RequestBody Map searchMap){
        return Result.SUCCESS("查询成功",scoreService.findSearch(searchMap));
    }

	/**
	 * 增加
	 * @param score
	 */
	@PostMapping()
	public Result add(@RequestBody Score score  ){
		scoreService.add(score);
		return Result.SUCCESS("添加成功");
	}

	/**
	 * 修改
	 * @param score
	 */
	@PutMapping(value="/{id}")
	public Result update(@RequestBody Score score, @PathVariable String id ){
		score.setId(id);
		scoreService.update(score);
		return Result.SUCCESS("修改成功");
	}

	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping(value="/{id}")
	public Result delete(@PathVariable String id ){
		scoreService.deleteById(id);
		return Result.SUCCESS("删除成功");
	}

	/**
	 * 导出
	 *
	 * @param response
	 */
	@GetMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response,String name) {
		EasyPoiUtil.exportExcel(response, scoreService.findAll(), Score.class,name);
	}

	/**
	 * 导入
	 *
	 * @param file
	 */
	@PostMapping("/importExcel")
	public Result importExcel(@RequestParam("file") MultipartFile file) {
		List list = EasyPoiUtil.importExcel(file, Score.class);
		if (list == null) {
			return Result.ERROR("导入失败");
		}
		for (Object object : list) {
			scoreService.add((Score) object);
		}
		return Result.SUCCESS("导入成功");
	}
}
