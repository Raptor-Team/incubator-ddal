package studio.raptor.ddal.dashboard.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liujy on 2017/11/27.
 * raptor-gid : BreadCrumb 方式序列
 */
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Sequence {

    public Sequence(String path, Long id, String name, Long incr, Long start, Integer cache, Long value) {
        super();
        this.path = path;
        this.id = id;
        this.name = name;
        this.incr = incr;
        this.start = start;
        this.cache = cache;
        this.value = value;
    }

    public Sequence(String name,Long value) {
        super();
        this.path = "";
        this.id = new Long(0);
        this.name = name;
        this.incr = new Long(0);;
        this.start = new Long(0);;
        this.cache = 0;
        this.value = value;
    }

    /**
     * 节点路径
     */
    private String path;
    /**
     * 当前序列值
     */
    private Long id;

    /**
     * 序列名
     */
    private String name;

    /**
     * 序列 增长步长
     */
    private Long incr;

    /**
     * 序列初始值
     */
    private Long start;

    /**
     * 序列缓存区间
     */
    private Integer cache;

    /**
     * 修改后的新值
     */
    private Long value;

    /**
     * 序列命名空间，即中心名
     */
    private String centerName;

    /**
     * 根据Excel重置时序列位置说明
     */
    private String position;
}
