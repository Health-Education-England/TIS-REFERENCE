import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	ReligiousBeliefService,
	ReligiousBeliefPopupService,
	ReligiousBeliefComponent,
	ReligiousBeliefDetailComponent,
	ReligiousBeliefDialogComponent,
	ReligiousBeliefPopupComponent,
	ReligiousBeliefDeletePopupComponent,
	ReligiousBeliefDeleteDialogComponent,
	religiousBeliefRoute,
	religiousBeliefPopupRoute,
	ReligiousBeliefResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...religiousBeliefRoute,
	...religiousBeliefPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ReligiousBeliefComponent,
		ReligiousBeliefDetailComponent,
		ReligiousBeliefDialogComponent,
		ReligiousBeliefDeleteDialogComponent,
		ReligiousBeliefPopupComponent,
		ReligiousBeliefDeletePopupComponent,
	],
	entryComponents: [
		ReligiousBeliefComponent,
		ReligiousBeliefDialogComponent,
		ReligiousBeliefPopupComponent,
		ReligiousBeliefDeleteDialogComponent,
		ReligiousBeliefDeletePopupComponent,
	],
	providers: [
		ReligiousBeliefService,
		ReligiousBeliefPopupService,
		ReligiousBeliefResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceReligiousBeliefModule {
}
