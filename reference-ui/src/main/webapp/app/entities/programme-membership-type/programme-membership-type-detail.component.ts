import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
import {ProgrammeMembershipTypeService} from "./programme-membership-type.service";

@Component({
	selector: 'jhi-programme-membership-type-detail',
	templateUrl: './programme-membership-type-detail.component.html'
})
export class ProgrammeMembershipTypeDetailComponent implements OnInit, OnDestroy {

	programmeMembershipType: ProgrammeMembershipType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeMembershipTypeService: ProgrammeMembershipTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['programmeMembershipType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.programmeMembershipTypeService.find(id).subscribe(programmeMembershipType => {
			this.programmeMembershipType = programmeMembershipType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
