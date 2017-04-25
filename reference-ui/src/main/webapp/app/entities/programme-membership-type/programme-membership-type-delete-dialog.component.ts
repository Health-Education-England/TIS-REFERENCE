import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
import {ProgrammeMembershipTypePopupService} from "./programme-membership-type-popup.service";
import {ProgrammeMembershipTypeService} from "./programme-membership-type.service";

@Component({
	selector: 'jhi-programme-membership-type-delete-dialog',
	templateUrl: './programme-membership-type-delete-dialog.component.html'
})
export class ProgrammeMembershipTypeDeleteDialogComponent {

	programmeMembershipType: ProgrammeMembershipType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeMembershipTypeService: ProgrammeMembershipTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programmeMembershipType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.programmeMembershipTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'programmeMembershipTypeListModification',
				content: 'Deleted an programmeMembershipType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-programme-membership-type-delete-popup',
	template: ''
})
export class ProgrammeMembershipTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmeMembershipTypePopupService: ProgrammeMembershipTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.programmeMembershipTypePopupService
				.open(ProgrammeMembershipTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
