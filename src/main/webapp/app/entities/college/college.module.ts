import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	CollegeService,
	CollegePopupService,
	CollegeComponent,
	CollegeDetailComponent,
	CollegeDialogComponent,
	CollegePopupComponent,
	CollegeDeletePopupComponent,
	CollegeDeleteDialogComponent,
	collegeRoute,
	collegePopupRoute,
	CollegeResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...collegeRoute,
	...collegePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		CollegeComponent,
		CollegeDetailComponent,
		CollegeDialogComponent,
		CollegeDeleteDialogComponent,
		CollegePopupComponent,
		CollegeDeletePopupComponent,
	],
	entryComponents: [
		CollegeComponent,
		CollegeDialogComponent,
		CollegePopupComponent,
		CollegeDeleteDialogComponent,
		CollegeDeletePopupComponent,
	],
	providers: [
		CollegeService,
		CollegePopupService,
		CollegeResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceCollegeModule {
}
