package studio.raptor.ddal.common.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class MultiKeyShardTarget extends HashMap<String, String>{
  private Map<String, String> _targetMap = new HashMap<>();

  @Override
  public String put(String key, String value) {
    return _targetMap.put(key, value);
  }

  @Override
  public String get(Object key) {
    return _targetMap.get(key);
  }

  //计算笛卡尔积
  public Collection<String> getAfterCartesian(String separator, Collection<String> availableTargetNames, Object[][] dimensionalGroup){
    Collection<String> result = new LinkedHashSet<>();
    for(int i=0; i<dimensionalGroup[0].length; i++){
      for(int j=0; j<dimensionalGroup[1].length; j++){
        for(String target : availableTargetNames){
          String suffix = separator + _targetMap.get(dimensionalGroup[0][i]+ separator +dimensionalGroup[1][j]);
          if (target.endsWith(suffix)) {
            result.add(target);
          }
        }

      }
    }
    return result;
  }
}
