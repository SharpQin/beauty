import { Component, Vue, Inject } from 'vue-property-decorator';

import { IRole } from '@/shared/model/auth/role.model';
import RoleService from './role.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class RoleDetails extends Vue {
  @Inject('roleService') private roleService: () => RoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public role: IRole = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.roleId) {
        vm.retrieveRole(to.params.roleId);
      }
    });
  }

  public retrieveRole(roleId) {
    this.roleService()
      .find(roleId)
      .then(res => {
        this.role = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
