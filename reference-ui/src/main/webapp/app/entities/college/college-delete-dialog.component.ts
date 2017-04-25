import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {College} from "./college.model";
import {CollegePopupService} from "./college-popup.service";
import {CollegeService} from "./college.service";

@Component({
	selector: 'jhi-college-delete-dialog',
	templateUrl: './college-delete-dialog.component.html'
})
export class CollegeDeleteDialogComponent {

	college: College;

	constructor(private jhiLanguageService: JhiLanguageService,
				private collegeService: CollegeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['college']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.collegeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'collegeListModification',
				content: 'Deleted an college'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-college-delete-popup',
	template: ''
})
export class CollegeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private collegePopupService: CollegePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.collegePopupService
				.open(CollegeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
