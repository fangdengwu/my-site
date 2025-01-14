package cn.luischen.controller.admin;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.controller.BaseController;
import cn.luischen.dto.cond.CommentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Comment;
import cn.luischen.model.User;
import cn.luischen.service.comment.CommentService;
import cn.luischen.utils.APIResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论
 * Created by winterchen on 2018/4/30.
 */
@Api("评论相关接口")
@Controller
@RequestMapping("/admin/comments")
@Slf4j
public class CommentController extends BaseController{

    @Autowired
    private CommentService commentService;

    @ApiOperation("进入评论列表页")
    @GetMapping(value = "")
    public String index(
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "每页条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
            int limit,
            HttpServletRequest request
    ){
        User user = this.user(request);

        Page<Comment> comments = commentService.getCommentsByCond(new CommentCond(), page, limit);
        request.setAttribute("comments", comments);
        return "admin/comment_list";
    }

    @ApiOperation("删除一条评论")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteComment(
            @ApiParam(name = "coid", value = "评论编号", required = true)
            @RequestParam(name = "coid", required = true)
            Integer coid
    ){

        try {
            Comment comment = commentService.getCommentById(coid);
            if (null == comment) {
                throw BusinessException.withErrorCode(ErrorConstant.Comment.COMMENT_NOT_EXIST);
            }

            commentService.deleteComment(coid);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }

    @ApiOperation("更改评论状态")
    @PostMapping(value = "/status")
    @ResponseBody
    public APIResponse changeStatus(
            @ApiParam(name = "coid", value = "评论主键", required = true)
            @RequestParam(name = "coid", required = true)
            Integer coid,
            @ApiParam(name = "status", value = "状态", required = true)
            @RequestParam(name = "status", required = true)
            String status
    ){
        try {
            Comment comment = commentService.getCommentById(coid);
            if (null != comment){
                commentService.updateCommentStatus(coid, status);
            }else{
                return APIResponse.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }




}
