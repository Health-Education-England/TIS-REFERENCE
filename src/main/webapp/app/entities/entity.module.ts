import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {ReferenceMaritalStatusModule} from "./marital-status/marital-status.module";
import {ReferenceAssessmentTypeModule} from "./assessment-type/assessment-type.module";
import {ReferenceCountryModule} from "./country/country.module";
import {ReferenceCurriculumSubTypeModule} from "./curriculum-sub-type/curriculum-sub-type.module";
import {ReferenceDBCModule} from "./dbc/dbc.module";
import {ReferenceEthnicOriginModule} from "./ethnic-origin/ethnic-origin.module";
import {ReferenceFundingIssueModule} from "./funding-issue/funding-issue.module";
import {ReferenceFundingTypeModule} from "./funding-type/funding-type.module";
import {ReferenceGdcStatusModule} from "./gdc-status/gdc-status.module";
import {ReferenceGenderModule} from "./gender/gender.module";
import {ReferenceGmcStatusModule} from "./gmc-status/gmc-status.module";
import {ReferenceGradeModule} from "./grade/grade.module";
import {ReferenceInactiveReasonModule} from "./inactive-reason/inactive-reason.module";
import {ReferenceLeavingDestinationModule} from "./leaving-destination/leaving-destination.module";
import {ReferenceLocalOfficeModule} from "./local-office/local-office.module";
import {ReferenceCollegeModule} from "./college/college.module";
import {ReferenceMedicalSchoolModule} from "./medical-school/medical-school.module";
import {ReferenceNationalityModule} from "./nationality/nationality.module";
import {ReferencePlacementTypeModule} from "./placement-type/placement-type.module";
import {ReferenceProgrammeMembershipTypeModule} from "./programme-membership-type/programme-membership-type.module";
import {ReferenceRecordTypeModule} from "./record-type/record-type.module";
import {ReferenceReligiousBeliefModule} from "./religious-belief/religious-belief.module";
import {ReferenceRoleModule} from "./role/role.module";
import {ReferenceSettledModule} from "./settled/settled.module";
import {ReferenceSexualOrientationModule} from "./sexual-orientation/sexual-orientation.module";
import {ReferenceSiteModule} from "./site/site.module";
import {ReferenceStatusModule} from "./status/status.module";
import {ReferenceTariffRateModule} from "./tariff-rate/tariff-rate.module";
import {ReferenceTitleModule} from "./title/title.module";
import {ReferenceTrainingNumberTypeModule} from "./training-number-type/training-number-type.module";
import {ReferenceTrustModule} from "./trust/trust.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
	imports: [
		ReferenceMaritalStatusModule,
		ReferenceAssessmentTypeModule,
		ReferenceCountryModule,
		ReferenceCurriculumSubTypeModule,
		ReferenceDBCModule,
		ReferenceEthnicOriginModule,
		ReferenceFundingIssueModule,
		ReferenceFundingTypeModule,
		ReferenceGdcStatusModule,
		ReferenceGenderModule,
		ReferenceGmcStatusModule,
		ReferenceGradeModule,
		ReferenceInactiveReasonModule,
		ReferenceLeavingDestinationModule,
		ReferenceLocalOfficeModule,
		ReferenceCollegeModule,
		ReferenceMedicalSchoolModule,
		ReferenceNationalityModule,
		ReferencePlacementTypeModule,
		ReferenceProgrammeMembershipTypeModule,
		ReferenceRecordTypeModule,
		ReferenceReligiousBeliefModule,
		ReferenceRoleModule,
		ReferenceSettledModule,
		ReferenceSexualOrientationModule,
		ReferenceSiteModule,
		ReferenceStatusModule,
		ReferenceTariffRateModule,
		ReferenceTitleModule,
		ReferenceTrainingNumberTypeModule,
		ReferenceTrustModule,
		/* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	],
	declarations: [],
	entryComponents: [],
	providers: [],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceEntityModule {
}
