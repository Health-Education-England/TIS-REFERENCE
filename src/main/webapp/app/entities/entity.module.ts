import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {ReferenceCurriculumTisProgrammesModule} from "./curriculum/curriculum-tis-programmes.module";
import {ReferenceGradeTisProgrammesModule} from "./grade/grade-tis-programmes.module";
import {ReferenceProgrammeTisProgrammesModule} from "./programme/programme-tis-programmes.module";
import {ReferenceProgrammeMembershipTisProgrammesModule} from "./programme-membership/programme-membership-tis-programmes.module";
import {ReferenceSpecialtyTisProgrammesModule} from "./specialty/specialty-tis-programmes.module";
import {ReferenceSpecialtyGroupTisProgrammesModule} from "./specialty-group/specialty-group-tis-programmes.module";
import {ReferenceTrainingNumberTisProgrammesModule} from "./training-number/training-number-tis-programmes.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
	imports: [
		ReferenceCurriculumTisProgrammesModule,
		ReferenceGradeTisProgrammesModule,
		ReferenceProgrammeTisProgrammesModule,
		ReferenceProgrammeMembershipTisProgrammesModule,
		ReferenceSpecialtyTisProgrammesModule,
		ReferenceSpecialtyGroupTisProgrammesModule,
		ReferenceTrainingNumberTisProgrammesModule,
		/* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	],
	declarations: [],
	entryComponents: [],
	providers: [],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceEntityModule {
}
